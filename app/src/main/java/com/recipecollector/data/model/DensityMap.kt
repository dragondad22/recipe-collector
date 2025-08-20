package com.recipecollector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "density_map")
data class DensityMap(
    @PrimaryKey val name: String,
    val gramsPerCup: Float? = null,
    val gramsPerTbsp: Float? = null,
    val gramsPerTsp: Float? = null
)