package com.example.dopaminemenu.vibemenu.model

data class Activity(
    var name : String ? = null,
    var desc : String? = null,
    var category : Category? = null,
    var state : ActivityState = ActivityState.pending

){}