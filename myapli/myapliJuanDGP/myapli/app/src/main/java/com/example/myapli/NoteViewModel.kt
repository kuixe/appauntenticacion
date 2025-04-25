import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData



class NoteViewModel : ViewModel() {
    private val _notes = MutableLiveData<List<String>>(listOf("Nota de ejemplo"))
    val notes: LiveData<List<String>> = _notes

    // Agregar una nueva nota
    fun addNote(note: String) {
        _notes.value = _notes.value?.plus(note)
    }

    // Obtener una nota por su índice
    fun getNote(index: Int): String {
        return _notes.value?.getOrNull(index) ?: "Nota no encontrada"
    }

    // Eliminar una nota por su índice
    fun deleteNote(index: Int) {
        _notes.value = _notes.value?.toMutableList()?.apply { removeAt(index) }
    }

    // Actualizar una nota por su índice
    fun updateNote(index: Int, newContent: String) {
        _notes.value = _notes.value?.toMutableList()?.apply { set(index, newContent) }
    }

    fun editarNota(notaVieja: String, notaNueva: String) {
        _notes.value = _notes.value?.map { if (it == notaVieja) notaNueva else it }
    }

}

