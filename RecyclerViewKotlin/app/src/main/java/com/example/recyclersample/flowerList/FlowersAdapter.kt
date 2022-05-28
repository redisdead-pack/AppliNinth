/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recyclersample.flowerList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.R
import com.example.recyclersample.data.Army
import kotlinx.android.synthetic.main.army_item.view.*


class FlowersAdapter(private val onClick: (Army) -> Unit) :
    ListAdapter<Army, FlowersAdapter.FlowerViewHolder>(FlowerDiffCallback) {

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class FlowerViewHolder(itemView: View, val onClick: (Army) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)
        private val flowerImageView: ImageView = itemView.findViewById(R.id.flower_image)
        private var currentArmy: Army? = null

        init {
            itemView.checkBoxPlus.setOnClickListener{
                currentArmy?.let {
                    (it).plus = itemView.checkBoxPlus.isChecked
                    (it).minus = itemView.checkBoxMinus.isChecked
                    (it).equal = itemView.checkBoxEqual.isChecked
                }
            }
            itemView.setOnClickListener {
                currentArmy?.let {
                    onClick(it)
                }
            }
        }

        /* Bind army name and image. */
        fun bind(army: Army) {
            currentArmy = army

            flowerTextView.text = army.name
            if (army.image != null) {
                flowerImageView.setImageResource(army.image)
            } else {
                flowerImageView.setImageResource(R.drawable.rose)
            }
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.army_item, parent, false)
        return FlowerViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        val flower = getItem(position)
        holder.bind(flower)

    }
}

object FlowerDiffCallback : DiffUtil.ItemCallback<Army>() {
    override fun areItemsTheSame(oldItem: Army, newItem: Army): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Army, newItem: Army): Boolean {
        return oldItem.id == newItem.id
    }
}