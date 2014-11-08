package com.explora.helper
import java.net.URLEncoder


object ExploraHelper {
    
   def format(text:String) = { URLEncoder.encode(text,"UTF-8")}
 
  }