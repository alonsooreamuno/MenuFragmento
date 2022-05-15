package cr.ac.menufragmentcurso

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
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
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val PICK_IMAGE2 = 100
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: String? = null
    private lateinit var image_avatar:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_add_empleado, container, false)

        val idEmpleado = view.findViewById<EditText>(R.id.addIDEmpleado)
        val nombreEmpleado = view.findViewById<EditText>(R.id.addNombreEmpleado)
        val puestoEmpleado = view.findViewById<EditText>(R.id.addPuestoEmpleado)
        val departamentoEmpleado = view.findViewById<EditText>(R.id.addDepartamentoEmpleado)
        image_avatar = view.findViewById<ImageView>(R.id.addImagen)
        val builder = AlertDialog.Builder(context)

        view.findViewById<Button>(R.id.buttonAgregarEmpleado).setOnClickListener {

            builder.setMessage("¿Desea agregar el empleado?")
                .setCancelable(false)
                .setPositiveButton("Sí"){
                        dialog, id ->
                    var newId:Int = EmpleadoRepository.instance.datos().size + 2
                    var imagenElegida:String = encodeImage(image_avatar.drawable.toBitmap())!!
                    val nuevoEmpleado = Empleado(newId,idEmpleado.getText().toString(),
                        nombreEmpleado.getText().toString(),puestoEmpleado.getText().toString(),
                        departamentoEmpleado.getText().toString(), imagenElegida)

                    EmpleadoRepository.instance.save(nuevoEmpleado)

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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: String) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, empleado)
                }
            }
    }
}