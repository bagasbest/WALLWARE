package com.application.wallware.catalogue

data class CatalogueModel(
    var uid: String? = null,
    var name: String? = null,
    var description: String? = null,
    var time: Int? = 0,
    var image: String? = null,
    var category: String? = null,
)