package com.evdealer.customer.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedback_id;

    private Long customer_id;
    private String feedback_type;
    private String subject;
    private String content;
    private Integer rating;
    private String status;

    @Column(name = "submission_date", insertable = false, updatable = false)
    private Timestamp submission_date;

    // getters and setters
    public Long getFeedback_id() { return feedback_id; }
    public void setFeedback_id(Long feedback_id) { this.feedback_id = feedback_id; }

    public Long getCustomer_id() { return customer_id; }
    public void setCustomer_id(Long customer_id) { this.customer_id = customer_id; }

    public String getFeedback_type() { return feedback_type; }
    public void setFeedback_type(String feedback_type) { this.feedback_type = feedback_type; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getSubmission_date() { return submission_date; }
    public void setSubmission_date(Timestamp submission_date) { this.submission_date = submission_date; }
}
