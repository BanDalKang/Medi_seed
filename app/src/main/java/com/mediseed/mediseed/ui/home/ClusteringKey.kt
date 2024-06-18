package com.mediseed.mediseed.ui.home

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

class ItemKey(val id: Int, private val position: LatLng) : ClusteringKey {
    override fun getPosition() = position

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as ItemKey
        return id == itemKey.id
    }

    override fun hashCode() = id
}