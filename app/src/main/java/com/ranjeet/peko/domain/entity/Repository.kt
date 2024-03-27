package com.ranjeet.peko.domain.entity

data class Repository(
    var name: String,
    var html_url: String,
    var description: String,
    var stargazers_count: Int,
    var forks_count: Int,
    var updated_at: String
)