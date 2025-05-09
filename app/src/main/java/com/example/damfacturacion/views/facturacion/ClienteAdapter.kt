package com.example.damfacturacion.views.facturacion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.R
import com.example.damfacturacion.model.Cliente

class ClienteAdapter(
    private var clientes: List<Cliente>,
    private val context: Context,
    private val onClienteClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEmpresaTextView: TextView = itemView.findViewById(R.id.textViewNombreEmpresa)
        val nifTextView: TextView = itemView.findViewById(R.id.textViewNIF)
        val telefonoTextView: TextView = itemView.findViewById(R.id.textViewTelefono)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClienteClick(clientes[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cliente_item, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.nombreEmpresaTextView.text = cliente.nombreEmpresa ?: "Sin nombre"
        holder.nifTextView.text = cliente.nif
        holder.telefonoTextView.text = cliente.telefono ?: "Sin teléfono"
    }

    override fun getItemCount(): Int = clientes.size

    // Método para actualizar los datos del adaptador
    fun actualizarClientes(nuevosClientes: List<Cliente>) {
        clientes = nuevosClientes
        notifyDataSetChanged()
    }
}
