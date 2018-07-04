package <%= packageName %>.core.messaging

interface Messager {
    fun send(topic: String, message: String)
}

interface MessagerFactory {
    fun instance(listener: MessagerGetListener)
}

interface MessagerGetListener{
    fun onMessagerGet(messager:Messager)
    fun onMessagerGetError(error:String)
}
