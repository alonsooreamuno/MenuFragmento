package cr.ac.menufragmentcurso

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "empleado"

/**
 * A simple [Fragment] subclass.
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

const val PICK_IMAGE = 100
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var image_avatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)

        val editID = view.findViewById<EditText>(R.id.editIDEmpleado)
        editID.setText(empleado?.identificacion)
        val editNombre = view.findViewById<EditText>(R.id.editNombreEmpleado)
        editNombre.setText(empleado?.nombre)
        val editPuesto = view.findViewById<EditText>(R.id.editPuestoEmpleado)
        editPuesto.setText(empleado?.puesto)
        val editDepartamento = view.findViewById<EditText>(R.id.editDepartamentoEmpleado)
        editDepartamento.setText(empleado?.departamento)
        image_avatar = view.findViewById(R.id.editImagenEmpleado)

        if(empleado?.avatar !=""){
            image_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }

        val builder = AlertDialog.Builder(context)

        view.findViewById<Button>(R.id.buttonModificar).setOnClickListener {
            builder.setMessage("¿Desea modificar los datos?")
                .setCancelable(false)
                .setPositiveButton("Sí"){
                    dialog, id ->

                    empleado?.identificacion = editID.text.toString()
                    empleado?.nombre = editNombre.text.toString()
                    empleado?.puesto = editPuesto.text.toString()
                    empleado?.departamento = editDepartamento.text.toString()
                    empleado?.avatar = encodeImage(image_avatar.drawable.toBitmap())!!

                    empleado?.let { it1 -> EmpleadoRepository.instance.edit(it1) }

                    val fragmento : Fragment = CameraFragment.newInstance("Cámara")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Cámara")

                }
                .setNegativeButton("No"){
                    dialog, id ->
                }
            val alert = builder.create()
            alert.show()
        }
        view.findViewById<Button>(R.id.buttonEliminar).setOnClickListener {
            builder.setMessage("¿Desea eliminar los datos?")
                .setCancelable(false)
                .setPositiveButton("Sí"){
                        dialog, id ->
                    empleado?.let { it1 -> EmpleadoRepository.instance.delete(it1) }

                    val fragmento : Fragment = CameraFragment.newInstance("Cámara")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Cámara")
                }
                .setNegativeButton("No"){
                        dialog, id ->
                }
            val alert = builder.create()
            alert.show()
        }
        image_avatar.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }
        return view
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int,data:Intent?){
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            val imageUri = data?.data
            Picasso.get()
                .load(imageUri)
                .resize(110,110)
                .centerCrop()
                .into(image_avatar)
        }
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }
    private fun decodeImage (b64 : String): Bitmap{
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empleado Parameter 1.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}