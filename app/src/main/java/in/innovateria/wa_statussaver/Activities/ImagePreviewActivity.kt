package `in`.innovateria.wa_statussaver.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import `in`.innovateria.wa_statussaver.Models.MediaModel
import `in`.innovateria.wa_statussaver.Utils.Constants
import `in`.innovateria.wa_statussaver.Adapters.ImagePreviewAdapter
import `in`.innovateria.wa_statussaver.databinding.ActivityImagePreviewBinding

class ImagePreviewActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy {
        ActivityImagePreviewBinding.inflate(layoutInflater)
    }
    lateinit var adapter: ImagePreviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            val list =
                intent.getSerializableExtra(Constants.MEDIA_LIST_KEY) as ArrayList<MediaModel>
            val scrollTo = intent.getIntExtra(Constants.MEDIA_SCROLL_KEY, 0)
            adapter = ImagePreviewAdapter(list, activity)
            imagesViewPager.adapter = adapter
            imagesViewPager.currentItem = scrollTo

            // Setup toolbar navigation
            toolBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

        }
    }
}