package com.januar.startupbaru

class Note {

    var noteID:Int?=null
    var noteName:String?=null
    var noteDes:String?=null

    constructor(noteID:Int, noteName:String, noteDes:String){
        this.noteID=noteID
        this.noteName=noteName
        this.noteDes=noteDes
    }
}