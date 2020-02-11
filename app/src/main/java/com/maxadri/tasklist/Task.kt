package com.maxadri.tasklist

import java.io.Serializable

data class Task(var id: String, var title: String, var description: String = "DescriptionDefault") : Serializable