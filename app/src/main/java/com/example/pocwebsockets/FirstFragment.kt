package com.example.pocwebsockets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.pocwebsockets.databinding.FragmentFirstBinding
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.tls.*
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient(OkHttp) {
            install(WebSockets)
            engine {
                // this: OkHttpConfig
                config {// this: OkHttpClient.Builderfollow
                    followRedirects(true)
                }
            }
        }

        runBlocking {
            client.webSocket(method = HttpMethod.Get, host = "145.93.128.195", port = 8001, path = "/quiz?name=poppetje&room=123") {
                while(true) {
                    val othersMessage = incoming.receive() as? Frame.Text
                    println(othersMessage?.readText())
                    val myMessage = Scanner(System.`in`).next()
                    if(myMessage != null)
                    {
                        send(myMessage)
                    }
                }
            }
        }
        client.close()

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}