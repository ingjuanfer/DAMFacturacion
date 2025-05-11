package com.example.damfacturacion.views.facturacion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.R
import com.example.damfacturacion.model.Cliente
import com.example.damfacturacion.model.ListaProductosBuscados
import com.example.damfacturacion.model.Producto
import com.example.damfacturacion.model.ProductoSeleccionado


class BuscadorProductosAdapter(
    private var productos: List<ListaProductosBuscados>,
    private val context: Context,
    private val onProductoClick: (ListaProductosBuscados) -> Unit
) : RecyclerView.Adapter<BuscadorProductosAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.textViewNombreProducto)
        val codProducto: TextView = itemView.findViewById(R.id.textCodProducto)
        val precio: TextView = itemView.findViewById(R.id.textViewPrecio)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onProductoClick(productos[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buscador_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombreProducto.text = producto.nombreProducto
        holder.codProducto.text = producto.codProducto
        holder.precio.text = String.format("%.2f", producto.precio)
    }

    override fun getItemCount(): Int = productos.size

    // Método para actualizar los datos del adaptador
    fun actualizarProductos(nuevosProductos: List<ListaProductosBuscados>) {
        productos = nuevosProductos
        notifyDataSetChanged()
    }


}
