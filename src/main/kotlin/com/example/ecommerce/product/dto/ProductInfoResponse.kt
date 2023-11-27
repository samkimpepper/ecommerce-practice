package com.example.ecommerce.product.dto

import com.example.ecommerce.product.document.Product
import com.example.ecommerce.product.document.ProductOption

data class ProductInfoResponse(
        val name: String,
        val minimumPrice: Int,
        val image: String?,
        val description: String?,
        val wishCount: Int,
        val stock: Int,
        var productOptions: List<ProductOptionInfo>?,
) {
    companion object {
        fun fromProduct(product: Product): ProductInfoResponse {
            return ProductInfoResponse(
                    name = product.name,
                    minimumPrice = product.minimumPrice,
                    image = product.image,
                    description = product.description?: null,
                    wishCount = product.wishCount,
                    stock = product.stock,
                    productOptions = mutableListOf(),
            )
        }
    }
}

data class ProductOptionInfo(
        val price: Int,
        val size: String,
        val stock: Int,
) {
    companion object {
        fun fromProductOptions(productOptions: List<ProductOption>): List<ProductOptionInfo> {
            return productOptions.map { option ->
                ProductOptionInfo(
                        price = option.price,
                        size = option.size,
                        stock = option.stock,
                )
            }
        }
    }
}