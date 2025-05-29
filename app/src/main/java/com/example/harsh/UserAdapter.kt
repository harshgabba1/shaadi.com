package com.example.harsh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class UserAdapter(
    private val users: MutableList<User>,
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.profileImage)
        val nameText: TextView = itemView.findViewById(R.id.profileName)
        val ageText: TextView = itemView.findViewById(R.id.profileDetails)
        val dislikeBtn: ImageButton = itemView.findViewById(R.id.btnDislike)
        val likeBtn: ImageButton = itemView.findViewById(R.id.btnLike)
        val textStatusBtn: TextView = itemView.findViewById(R.id.textStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.nameText.text = "${user.name.first} ${user.name.last}"
        holder.ageText.text = "${user.dob.age},${user.location.city}, ${user.location.state}"
        holder.dislikeBtn.setOnClickListener({
            Toast.makeText(context, "Declined", Toast.LENGTH_SHORT).show()
            holder.dislikeBtn.visibility = View.GONE
            holder.textStatusBtn.visibility = View.VISIBLE
            holder.textStatusBtn.text = "Declined"
            user.isButtonCLicked = true
        })
        holder.likeBtn.setOnClickListener({
            Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show()
            holder.dislikeBtn.visibility = View.GONE
            holder.textStatusBtn.visibility = View.VISIBLE
            holder.textStatusBtn.text = "Accepted"
            user.accepted = true
            user.isButtonCLicked = true
        })

        Glide.with(holder.itemView)
            .load(user.picture.large)
            .into(holder.imageView)

//        lifecycleScope.launch {
//            userDao.insert(
//                user
//            )
//        }
    }

//    fun addUsers(newUsers: List<User>) {
//        val start = users.size
//        users.addAll(newUsers)
//        notifyItemRangeInserted(start, newUsers.size)
//    }
}
