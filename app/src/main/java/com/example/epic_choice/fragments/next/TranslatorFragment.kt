package com.example.epic_choice.fragments.next

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.epic_choice.R
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class TranslatorFragment : Fragment() {

    private lateinit var sourceText: EditText
    private lateinit var targetLanguageSpinner: Spinner
    private lateinit var translateButton: Button
    private lateinit var translatedTextView: TextView
    private lateinit var backButton: Button
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_translator, container, false)

        // Initialize UI elements
        sourceText = view.findViewById(R.id.source_text)
        targetLanguageSpinner = view.findViewById(R.id.target_language)
        translateButton = view.findViewById(R.id.translate_button)
        translatedTextView = view.findViewById(R.id.translated_text)
        backButton = view.findViewById(R.id.translatorbackbutton)

        // Initialize the target language spinner
        val languageArray = resources.getStringArray(R.array.language_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languageArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetLanguageSpinner.adapter = adapter

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext())

        translateButton.setOnClickListener {
            val textToTranslate = sourceText.text.toString()
            if (textToTranslate.isNotEmpty()) {
                val targetLang = getLanguageCode(targetLanguageSpinner.selectedItem.toString())
                detectAndTranslateText(textToTranslate, "auto", targetLang)
            } else {
                Toast.makeText(requireContext(), "Please enter text to translate", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    // Method to get the language code from the language name
    private fun getLanguageCode(languageName: String): String {
        return when (languageName) {
            "English" -> "en"
            "Spanish" -> "es"
            "French" -> "fr"
            "German" -> "de"
            else -> "en" // Default to English
        }
    }

    // Method to detect and translate the text
    private fun detectAndTranslateText(text: String, sourceLang: String, targetLang: String) {
        try {
            val encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString())
            val url = "https://lingva.ml/api/v1/$sourceLang/$targetLang/$encodedText"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        // The response is expected to be a JSON object
                        val jsonResponse = JSONObject(response)
                        val translatedText = jsonResponse.getString("translation")
                        translatedTextView.text = translatedText
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Parsing error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(requireContext(), "Translation failed: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )

            // Add the request to the RequestQueue
            requestQueue.add(stringRequest)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Encoding error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
