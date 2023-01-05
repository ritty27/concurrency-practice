package com.ritty27.dblock.entity

import javax.persistence.*


@Entity
@Table(name = "account")
class AccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,

    var money: Long

) {
    fun decreaseMoney(fare: Long) {
        money -= fare
    }
}
