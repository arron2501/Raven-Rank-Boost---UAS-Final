package com.application.ravenrankboost.View.Dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.ravenrankboost.Helper.SessionManager
import com.application.ravenrankboost.R
import com.application.ravenrankboost.View.ListOrder.ListOrderActivity
import com.application.ravenrankboost.View.NewOrder.NewOrderActivity
import com.application.ravenrankboost.View.Notifications.NotificationActivity
import com.application.ravenrankboost.View.ProfileActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var orderAdapter: OrderAdapter
    lateinit var newsAdapter: NewsAdapter
    lateinit var testiAdapter: TestiAdapter
    lateinit var orderLayoutManager: LinearLayoutManager
    lateinit var newsLayoutManager: LinearLayoutManager
    lateinit var testiLayoutManager: LinearLayoutManager
    lateinit var sessionManager: SessionManager
    var list: ArrayList<Int> = ArrayList()
    var listHowToOrder: ArrayList<Int> = ArrayList()
    var listTesti: ArrayList<Int> = ArrayList()
    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        rvOrder.adapter = orderAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sessionManager = SessionManager(this)

        btNewOrder.setOnClickListener(this)
        btListOrder.setOnClickListener(this)
        btNotification.setOnClickListener(this)
        tvManage.setOnClickListener(this)
        setUpRecyclerView()
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val date = Date()
        val year = SimpleDateFormat("yyyy", Locale.US).format(Date())

        tvDate.text = "${date.date} ${monthNames[date.month]} $year"
        val c = Calendar.getInstance()
        when (c[Calendar.HOUR_OF_DAY]) {
            in 0..11 -> {
                tvGood.text = "GOOD MORNING,"
            }
            in 12..15 -> {
                tvGood.text = "GOOD AFTERNOON,"
            }
            in 16..20 -> {
                tvGood.text = "GOOD EVENING,"
            }
            in 21..23 -> {
                tvGood.text = "GOOD NIGHT,"
            }
        }
        tvName.text = "${sessionManager.getStringFromSP("USERNAME")}!"
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btNewOrder -> startActivity(
                Intent(
                    this@DashboardActivity,
                    NewOrderActivity::class.java
                )
            )
            R.id.btListOrder -> startActivity(
                Intent(
                    this@DashboardActivity,
                    ListOrderActivity::class.java
                )
            )
            R.id.btNotification -> startActivity(
                Intent(
                    this@DashboardActivity,
                    NotificationActivity::class.java
                )
            )
            R.id.tvManage -> startActivity(
                Intent(
                    this@DashboardActivity,
                    ProfileActivity::class.java
                )
            )
        }
    }

    fun setUpRecyclerView() {
        list.add(R.drawable.news1)
        list.add(R.drawable.news2)
        list.add(R.drawable.news3)
        listHowToOrder.add(R.drawable.step1)
        listHowToOrder.add(R.drawable.step2)
        listHowToOrder.add(R.drawable.step3)
        listTesti.add(R.drawable.t1)
        listTesti.add(R.drawable.t2)
        listTesti.add(R.drawable.t3)

        newsAdapter = NewsAdapter(this, list)
        rvNews.adapter = newsAdapter
        newsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvNews.layoutManager = newsLayoutManager

        orderAdapter = OrderAdapter(this, listHowToOrder)
        rvOrder.adapter = orderAdapter
        orderLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvOrder.layoutManager = orderLayoutManager

        testiAdapter = TestiAdapter(this, listTesti)
        rvTesti.adapter = testiAdapter
        testiLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTesti.layoutManager = testiLayoutManager

        val orderAdapter = OrderAdapter(this, listHowToOrder)
        rvOrder.adapter = orderAdapter
        setAutoScroll(rvOrder)
    }

    fun setAutoScroll(rv: RecyclerView) {
        val pixelsToMove = 50
        val mHandler = Handler(Looper.getMainLooper())
        val SCROLLING_RUNNABLE: Runnable = object : Runnable {
            override fun run() {
                rv.smoothScrollBy(pixelsToMove, 0)
                mHandler.postDelayed(this, 50)
            }
        }
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem: Int = orderLayoutManager.findLastCompletelyVisibleItemPosition()
                if (lastItem == orderLayoutManager.getItemCount() - 1) {
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE)
                    val postHandler = Handler()
                    postHandler.postDelayed({
                        rv.adapter = null
                        rv.adapter = orderAdapter
                        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000)
                    }, 2000)
                }
            }
        }
        rv.addOnScrollListener(scrollListener)
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000)

        rv.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                mHandler.removeCallbacks(SCROLLING_RUNNABLE)
                rv.removeOnScrollListener(scrollListener)
                // Do what you want
                true
            } else false
        })
    }


}