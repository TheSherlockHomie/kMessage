package com.thesherlockhomie.kmessage

class Message(
    val id: String,
    val text: String,
    val from: String,
    val to: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", "", -1)
}