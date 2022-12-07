package com.turgutkurt.retrofitkotlinkripto.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turgutkurt.retrofitkotlinkripto.adapter.RecyclerViewAdapter
import com.turgutkurt.retrofitkotlinkripto.databinding.ActivityMainBinding
import com.turgutkurt.retrofitkotlinkripto.model.CryptoModel
import com.turgutkurt.retrofitkotlinkripto.services.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : AppCompatActivity() , RecyclerViewAdapter.Listener {
    private lateinit var binding: ActivityMainBinding
    private val baseUrl="https://raw.githubusercontent.com/"
    private var cryptoModels : ArrayList<CryptoModel>? =null
    private var recyclerViewAdapter : RecyclerViewAdapter? =null
    //Disposable kullan at
    private var compositeDisposable :CompositeDisposable? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        compositeDisposable = CompositeDisposable()
        var layoutManager :RecyclerView.LayoutManager=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        loadData();
    }
    private fun loadData(){
        println("loadData")
        val retrofit =Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        println("compositeDisposable")
        compositeDisposable?.add(retrofit
            .getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse)
        )
        /*
        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()
        call.enqueue(object:Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if (response.isSuccessful){
                    //eğer ki bu boş gelmediyse bu işlemi yap
                    println("response.isSuccessful ${response.body()}")
                    response.body()?.let{
                        cryptoModels = ArrayList(it)
                        cryptoModels.let {
                            recyclerViewAdapter=RecyclerViewAdapter(cryptoModels!!,this@MainActivity)
                            binding.recyclerView.adapter= recyclerViewAdapter
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
         */
    }
    private fun handleResponse (cryptoList:List<CryptoModel>){
        println("handleResponse")
        cryptoModels = ArrayList(cryptoList)
        cryptoModels.let {
            recyclerViewAdapter=RecyclerViewAdapter(cryptoModels!!,this@MainActivity)
            binding.recyclerView.adapter= recyclerViewAdapter
        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
       Toast.makeText(this,"clicked : ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}


