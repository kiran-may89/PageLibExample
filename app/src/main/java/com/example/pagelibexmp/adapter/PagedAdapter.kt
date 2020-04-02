package com.example.pagelibexmp.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagelibexmp.R
import com.example.pagelibexmp.data.FeedDataSource
import com.example.pagelibexmp.databinding.ItemFeedBinding
import com.example.pagelibexmp.databinding.ItemNetworkStateBinding
import com.example.pagelibexmp.models.Article
import com.example.pagelibexmp.services.Api
import com.example.pagelibexmp.services.ServicesConfig
import com.example.pagelibexmp.utils.AppUtils
import com.squareup.picasso.Picasso

class PagedAdapter() :
    PagedListAdapter<Article, RecyclerView.ViewHolder>(DIFF_UTIL) {
      var networkState: String?= null
    companion object {
        private val TYPE_PROGRESS: Int = 0;
        private val TYPE_ITEM: Int = 1;

        private val DIFF_UTIL: DiffUtil.ItemCallback<Article> =
            object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                    (oldItem.id == newItem.id)

                override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                    (oldItem == newItem)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PROGRESS) {
            val networkState: ItemNetworkStateBinding =
                DataBindingUtil.inflate<ItemNetworkStateBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_network_state,
                    parent,
                    false
                )
            NetworkStateViewHolder(networkState)
        } else {
            val itemFeedBinding: ItemFeedBinding = DataBindingUtil.inflate<ItemFeedBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_feed,
                parent,
                false
            )
            PagedViedHolder(itemFeedBinding)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PagedViedHolder){
           getItem(position)?.let {
               holder.bind(it)
           }
        }else{
            networkState?.let {
                val view =   holder as NetworkStateViewHolder
                view.bind(it)
            }

        }

    }

    private fun hasExtraRow(): Boolean {
        return networkState !=null && !networkState.equals(FeedDataSource.SUCCESS)
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS;
        } else {
            TYPE_ITEM
        }
    }

    public fun setState(state: String) {
        val isExtraRowPresents = hasExtraRow()
        this.networkState = state;
        val nextRow = hasExtraRow()
        if (isExtraRowPresents != nextRow) {
            if (isExtraRowPresents) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (nextRow && networkState != state) {
            notifyItemChanged(itemCount - 1)
        }

    }


    class PagedViedHolder(val item: ItemFeedBinding) : RecyclerView.ViewHolder(item.rootView) {
        fun bind(article: Article) {
            item.itemImage.visibility = (View.VISIBLE)
            item.itemDesc.visibility = View.VISIBLE
            val author = article.author ?: item.rootView.context.getString(R.string.author_name)
            val titleString = String.format(
                item.rootView.context.getString(R.string.item_title),
                author,
                article.title
            )
            val spanString = SpannableString(titleString);
            spanString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        item.rootView.context,
                        R.color.secondary_text
                    )
                ),
                titleString.lastIndexOf(author) + author.length + 1,
                titleString.lastIndexOf(article.title) - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            item.itemTitle.text = spanString
            item.itemTime.text = String.format(
                item.rootView.context.getString(R.string.item_date),
                AppUtils.getDate(article.publishedAt), AppUtils.getTime(article.publishedAt)
            );
            Picasso.get().load(article.urlToImage).resize(250, 200).into(item.itemImage)


        }

    }

    class NetworkStateViewHolder(val itemview: ItemNetworkStateBinding) :
        RecyclerView.ViewHolder(itemview.root) {

        fun bind(networkState: String) {
            if(networkState == FeedDataSource.LOADING){
                itemview.progressBar.visibility = View.VISIBLE

            }else{
                itemview.progressBar.visibility = View.GONE
            }

            if (networkState == FeedDataSource.FAILED){
                itemview.errorMsg.visibility = View.VISIBLE
                itemview.errorMsg.text = "SomeThing Went Wrong"
            }else{
                itemview.errorMsg.visibility = View.GONE
            }

        }

    }
}
