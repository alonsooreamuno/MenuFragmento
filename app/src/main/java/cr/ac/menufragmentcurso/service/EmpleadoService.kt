package cr.ac.menufragmentcurso.service

import cr.ac.menufragmentcurso.entity.Empleado
import retrofit2.Call
import cr.ac.menufragmentcurso.entity.Record
import retrofit2.http.*


interface EmpleadoService {
    @GET ( "empleado")
    fun getEmpleado(): Call<Record>

    @PUT( "empleado/{idEmpleado}")
    fun updateEmpleado(@Path("idEmpleado") idEmpleado:Int, @Body empleado: Empleado) : Call<String>

    @DELETE( "empleado/{idEmpleado}")
    fun deleteEmpleado(@Path("idEmpleado") idEmpleado:Int) : Call<String>

    @POST( "empleado")
    fun saveEmpleado(@Body empleado: Empleado) : Call<String>
}