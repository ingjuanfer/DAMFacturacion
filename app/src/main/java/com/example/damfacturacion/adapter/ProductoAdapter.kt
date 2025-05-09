package com.example.damfacturacion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damfacturacion.R
import com.example.damfacturacion.model.Producto

class ProductoAdapter(private var productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Aquí usamos findViewById para obtener las vistas del layout del item
        val nombreProducto: TextView = itemView.findViewById(R.id.textViewNombreProducto)
        val precio: TextView = itemView.findViewById(R.id.textViewPrecio)
        val iva: TextView = itemView.findViewById(R.id.textViewIva)
        // Puedes agregar más vistas según sea necesario, por ejemplo, la imagen del producto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        // Inflamos el layout del item del producto
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_seleccionado, parent, false)
        return ProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        // Asignamos los datos del producto a las vistas
        holder.nombreProducto.text = producto.nombreProducto
        holder.precio.text = "Precio: \$${producto.precio}"
        holder.iva.text = "IVA: ${producto.iva}%"
        // Si tienes imagenes, puedes usar una librería como Glide o Picasso para cargar la imagen
    }

    override fun getItemCount(): Int = productos.size

    // Método para actualizar la lista de productos
    fun submitList(newProductos: List<Producto>) {
        productos = newProductos
        notifyDataSetChanged()
    }
}
