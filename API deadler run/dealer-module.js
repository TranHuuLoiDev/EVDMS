/*
Dealer API module for Electric Vehicle Dealer Management System
Node.js + Express + Mongoose (single-file example module)
Usage: mount the exported router in your Express app: app.use('/api/dealer', require('./dealer-module'))
This file provides models, controllers, routes for Dealer Staff / Dealer Manager features:
- Vehicle queries
- Sales management (Quotes, Orders, Contracts)
- Customer management
- Simple report endpoints

Note: This is an example starter module. In production split into files, add error handling, logging, and tests.
*/

const express = require('express');
const mongoose = require('mongoose');
const Joi = require('joi');
const router = express.Router();

// ---------------------------
// Simple auth middleware (placeholder)
// ---------------------------
function auth(requiredRoles = []) {
  return (req, res, next) => {
    // Placeholder: replace with real JWT/session auth
    // Example: req.user = { id: 'u1', role: 'DealerStaff', dealerId: 'd1' }
    req.user = req.user || { id: 'system', role: 'Admin', dealerId: null };
    if (requiredRoles.length && !requiredRoles.includes(req.user.role)) return res.status(403).json({ error: 'Forbidden' });
    next();
  };
}

// ---------------------------
// Mongoose models (simple)
// ---------------------------
const { Schema } = mongoose;

const VehicleSchema = new Schema({
  sku: { type: String, index: true },
  model: String,
  version: String,
  color: String,
  specs: Schema.Types.Mixed, // e.g. battery, range, power
  msrp: Number,
  stockTotal: { type: Number, default: 0 },
  stockByDealer: [{ dealerId: String, qty: Number }],
  createdAt: { type: Date, default: Date.now }
});

const CustomerSchema = new Schema({
  name: String,
  phone: String,
  email: String,
  address: String,
  notes: String,
  createdAt: { type: Date, default: Date.now }
});

const QuoteSchema = new Schema({
  dealerId: String,
  createdBy: String,
  customerId: { type: Schema.Types.ObjectId, ref: 'Customer' },
  items: [{ vehicleSku: String, price: Number, qty: Number }],
  discounts: Schema.Types.Mixed,
  total: Number,
  status: { type: String, enum: ['Draft','Sent','Accepted','Rejected'], default: 'Draft' },
  createdAt: { type: Date, default: Date.now }
});

const OrderSchema = new Schema({
  dealerId: String,
  createdBy: String,
  customerId: { type: Schema.Types.ObjectId, ref: 'Customer' },
  quoteId: { type: Schema.Types.ObjectId, ref: 'Quote' },
  items: [{ vehicleSku: String, price: Number, qty: Number }],
  payment: { method: String, status: String, plan: Schema.Types.Mixed },
  delivery: { status: String, eta: Date, tracking: String },
  status: { type: String, enum: ['Pending','Processing','Delivered','Cancelled'], default: 'Pending' },
  createdAt: { type: Date, default: Date.now }
});

// register models (avoid overwrite if already registered)
const Vehicle = mongoose.models.Vehicle || mongoose.model('Vehicle', VehicleSchema);
const Customer = mongoose.models.Customer || mongoose.model('Customer', CustomerSchema);
const Quote = mongoose.models.Quote || mongoose.model('Quote', QuoteSchema);
const Order = mongoose.models.Order || mongoose.model('Order', OrderSchema);

// ---------------------------
// Validation schemas (Joi)
// ---------------------------
const vehicleQuerySchema = Joi.object({
  q: Joi.string().allow('', null),
  model: Joi.string().allow('', null),
  minPrice: Joi.number().min(0),
  maxPrice: Joi.number().min(0),
  page: Joi.number().min(1).default(1),
  limit: Joi.number().min(1).max(200).default(20)
});

const customerSchemaJoi = Joi.object({ name: Joi.string().required(), phone: Joi.string().allow('', null), email: Joi.string().email().allow('', null), address: Joi.string().allow('', null), notes: Joi.string().allow('', null) });

const quoteCreateSchema = Joi.object({ dealerId: Joi.string().required(), customerId: Joi.string().required(), items: Joi.array().items(Joi.object({ vehicleSku: Joi.string().required(), price: Joi.number().min(0).required(), qty: Joi.number().min(1).required() })), discounts: Joi.any() });

const orderCreateSchema = Joi.object({ dealerId: Joi.string().required(), customerId: Joi.string().required(), items: Joi.array().items(Joi.object({ vehicleSku: Joi.string().required(), price: Joi.number().min(0).required(), qty: Joi.number().min(1).required() })), payment: Joi.any() });

// ---------------------------
// Controllers / Route Handlers
// ---------------------------

// --- Vehicles ---
router.get('/vehicles', auth(['DealerStaff','DealerManager','Admin','EVMStaff']), async (req, res) => {
  try {
    const params = await vehicleQuerySchema.validateAsync(req.query, { allowUnknown: true });
    const filter = {};
    if (params.q) filter.$or = [{ model: new RegExp(params.q, 'i') }, { version: new RegExp(params.q, 'i') }, { sku: new RegExp(params.q, 'i') }];
    if (params.model) filter.model = params.model;
    if (params.minPrice) filter.msrp = { $gte: params.minPrice };
    if (params.maxPrice) filter.msrp = Object.assign(filter.msrp||{}, { $lte: params.maxPrice });

    const skip = (params.page - 1) * params.limit;
    const [items, total] = await Promise.all([
      Vehicle.find(filter).skip(skip).limit(params.limit).lean(),
      Vehicle.countDocuments(filter)
    ]);
    res.json({ meta: { total, page: params.page, limit: params.limit }, data: items });
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

router.get('/vehicles/:sku', auth(['DealerStaff','DealerManager','Admin','EVMStaff']), async (req, res) => {
  const sku = req.params.sku;
  const v = await Vehicle.findOne({ sku }).lean();
  if (!v) return res.status(404).json({ error: 'Vehicle not found' });
  res.json(v);
});

// --- Customers ---
router.post('/customers', auth(['DealerStaff','DealerManager']), async (req, res) => {
  try {
    const payload = await customerSchemaJoi.validateAsync(req.body, { allowUnknown: true });
    const c = new Customer(payload);
    await c.save();
    res.status(201).json(c);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

router.get('/customers/:id', auth(['DealerStaff','DealerManager']), async (req, res) => {
  try {
    const c = await Customer.findById(req.params.id).lean();
    if (!c) return res.status(404).json({ error: 'Customer not found' });
    res.json(c);
  } catch (err) { res.status(400).json({ error: err.message }); }
});

router.get('/customers', auth(['DealerStaff','DealerManager']), async (req, res) => {
  const q = req.query.q || '';
  const items = await Customer.find({ $or: [{ name: new RegExp(q,'i') }, { phone: new RegExp(q,'i') }, { email: new RegExp(q,'i') }] }).limit(50).lean();
  res.json(items);
});

// --- Quotes ---
router.post('/quotes', auth(['DealerStaff','DealerManager']), async (req, res) => {
  try {
    const payload = await quoteCreateSchema.validateAsync(req.body, { allowUnknown: true });
    const total = payload.items.reduce((s, it) => s + (it.price * it.qty), 0);
    const doc = new Quote(Object.assign({}, payload, { createdBy: req.user.id, total }));
    await doc.save();
    res.status(201).json(doc);
  } catch (err) { res.status(400).json({ error: err.message }); }
});

router.get('/quotes/:id', auth(['DealerStaff','DealerManager']), async (req, res) => {
  try {
    const q = await Quote.findById(req.params.id).populate('customerId').lean();
    if (!q) return res.status(404).json({ error: 'Quote not found' });
    res.json(q);
  } catch (err) { res.status(400).json({ error: err.message }); }
});

// Update quote status (e.g., Sent, Accepted)
router.patch('/quotes/:id/status', auth(['DealerStaff','DealerManager']), async (req, res) => {
  const { status } = req.body;
  if (!['Draft','Sent','Accepted','Rejected'].includes(status)) return res.status(400).json({ error: 'Invalid status' });
  const q = await Quote.findByIdAndUpdate(req.params.id, { status }, { new: true }).lean();
  if (!q) return res.status(404).json({ error: 'Quote not found' });
  res.json(q);
});

// --- Orders ---
router.post('/orders', auth(['DealerStaff','DealerManager']), async (req, res) => {
  try {
    const payload = await orderCreateSchema.validateAsync(req.body, { allowUnknown: true });
    const order = new Order(Object.assign({}, payload, { createdBy: req.user.id }));
    await order.save();
    // Optionally decrement dealer stock here
    res.status(201).json(order);
  } catch (err) { res.status(400).json({ error: err.message }); }
});

router.get('/orders/:id', auth(['DealerStaff','DealerManager','Admin']), async (req, res) => {
  try {
    const o = await Order.findById(req.params.id).populate('customerId quoteId').lean();
    if (!o) return res.status(404).json({ error: 'Order not found' });
    res.json(o);
  } catch (err) { res.status(400).json({ error: err.message }); }
});

router.patch('/orders/:id/status', auth(['DealerStaff','DealerManager','Admin']), async (req, res) => {
  const { status } = req.body;
  if (!['Pending','Processing','Delivered','Cancelled'].includes(status)) return res.status(400).json({ error: 'Invalid status' });
  const o = await Order.findByIdAndUpdate(req.params.id, { status }, { new: true }).lean();
  if (!o) return res.status(404).json({ error: 'Order not found' });
  res.json(o);
});

// Payment update
router.patch('/orders/:id/payment', auth(['DealerStaff','DealerManager','Admin']), async (req, res) => {
  const { payment } = req.body;
  const o = await Order.findByIdAndUpdate(req.params.id, { payment }, { new: true }).lean();
  if (!o) return res.status(404).json({ error: 'Order not found' });
  res.json(o);
});

// --- Reports (basic) ---
router.get('/reports/sales-by-staff', auth(['DealerManager','Admin','EVMStaff']), async (req, res) => {
  const { dealerId, from, to } = req.query;
  const match = { createdAt: {} };
  if (dealerId) match.dealerId = dealerId;
  if (from) match.createdAt.$gte = new Date(from);
  if (to) match.createdAt.$lte = new Date(to);
  // aggregate orders by createdBy
  const agg = await Order.aggregate([
    { $match: match },
    { $unwind: '$items' },
    { $group: { _id: '$createdBy', sales: { $sum: { $multiply: ['$items.price', '$items.qty'] } }, count: { $sum: 1 } } },
    { $sort: { sales: -1 } }
  ]);
  res.json(agg);
});

router.get('/reports/aging-receivables', auth(['DealerManager','Admin','EVMStaff']), async (req, res) => {
  // Example: list orders with payment.status !== 'Paid'
  const items = await Order.find({ 'payment.status': { $ne: 'Paid' } }).limit(200).lean();
  res.json(items.map(o => ({ id: o._id, dealerId: o.dealerId, total: o.items.reduce((s,i)=>s+i.price*i.qty,0), payment: o.payment, createdAt: o.createdAt })));
});

// ---------------------------
// Helper route to seed sample vehicle (dev only)
// ---------------------------
router.post('/_seed/vehicle', auth(['Admin']), async (req, res) => {
  const v = new Vehicle(req.body);
  await v.save();
  res.json(v);
});

// Export router
module.exports = router;

// If run directly, demonstrate simple server (for quick testing)
if (require.main === module) {
  (async () => {
    try {
      await mongoose.connect(process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/evdealer', { useNewUrlParser: true, useUnifiedTopology: true });
      const app = express();
      app.use(express.json());
      app.use('/api/dealer', router);
      const port = process.env.PORT || 3000;
      app.listen(port, () => console.log('Dealer API running on port', port));
    } catch (err) {
      console.error('Failed to start demo server', err);
    }
  })();
}
