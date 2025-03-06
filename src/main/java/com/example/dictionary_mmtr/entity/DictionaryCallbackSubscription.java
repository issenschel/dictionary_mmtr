package com.example.dictionary_mmtr.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "dictionary_callback_subscription")
public class DictionaryCallbackSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id")
    private UUID subscriptionId;

    @ManyToOne
    @JoinColumn(name = "dictionary_type_id")
    private DictionaryType dictionaryType;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "access_token")
    private String accessToken;
}
