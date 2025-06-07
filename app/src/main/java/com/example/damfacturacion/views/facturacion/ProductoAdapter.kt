package com.example.damfacturacion.views.facturacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.R
import com.example.damfacturacion.model.ProductoSeleccionado

class ProductoAdapter(private val productos: MutableList<ProductoSeleccionado>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_seleccionado, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombreProducto.text = producto.nombreProducto
        holder.codProducto.text = producto.codProducto
        holder.cantidad.text = producto.cantidad.toString()
        holder.iva.text = "${producto.iva}%"  // Asumiendo que es un número
        holder.precio.text = String.format("%.2f", producto.precio)
    }
    override fun getItemCount(): Int = productos.size

    fun agregarProducto(producto: ProductoSeleccionado) {
        productos.add(producto)
        notifyItemInserted(productos.size - 1)
    }

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.textViewNombreProducto)
        val codProducto: TextView = itemView.findViewById(R.id.textViewcodProducto)
        val cantidad: TextView = itemView.findViewById(R.id.textViewCantidad)
        val iva: TextView = itemView.findViewById(R.id.textViewIva)
        val precio: TextView = itemView.findViewById(R.id.textViewPrecio)
    }
}
