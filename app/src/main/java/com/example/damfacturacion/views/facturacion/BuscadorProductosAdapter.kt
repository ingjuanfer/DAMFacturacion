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

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombreProducto: TextView = itemView.findViewById(R.id.textViewNombreProducto)
        val textViewCodProducto: TextView = itemView.findViewById(R.id.textViewcodProducto)
        val textViewIva: TextView = itemView.findViewById(R.id.textViewIva)
        val textViewPrecio: TextView = itemView.findViewById(R.id.textViewPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buscador_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.textViewNombreProducto.text = producto.nombreProducto ?: "Sin nombre"
        holder.textViewCodProducto.text = producto.codProducto
        holder.textViewIva.text = String.format("%.2f%%", producto.iva ?: 0.0)
        holder.textViewPrecio.text = String.format("%.2f €", producto.precio ?: 0.0)
    }

    override fun getItemCount(): Int = productos.size

    // Método para actualizar los datos del adaptador
    fun actualizarProductos(nuevosProductos: List<ListaProductosBuscados>) {
        productos = nuevosProductos
        notifyDataSetChanged()
    }

    fun limpiarProductos() {
        productos = emptyList()
        notifyDataSetChanged()
    }



}
