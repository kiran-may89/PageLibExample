package com.example.pagelibexmp.models

data class Feed (var id:Long,var status:String,var totalResults:Long, public var articles:List<Article>){
}