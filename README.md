# NamuFinder
  - 브라우저 들어갈 필요없이 네이티브에서 바로 나무위키를 사용하자

# Used
  1. Android Studio 4.2 (API Level 30)
  2. Kotlin 1.3.2 (Data Binding, Web View, RecyclerView)
  3. Jsoup 1.13.1
  4. Room Database
  
# Running Capture
![image](https://user-images.githubusercontent.com/65227900/114378337-93c42c80-9bc2-11eb-85f4-7e41bc2e1252.png)
  1. Data Binding을 통해 EditText의 Text 수의 따라 동적으로 View들을 필요에 따라 표현
  2. 크롤링을 통해서 가져온 Elements를 RecyclerView에 동적으로 구현

![image](https://user-images.githubusercontent.com/65227900/114378653-e30a5d00-9bc2-11eb-801d-958304d7352c.png)
  1. RecyclerView에 구현된 Item을 클릭하여 WebView에서 실행되도록 구현
 
![image](https://user-images.githubusercontent.com/65227900/114378991-3e3c4f80-9bc3-11eb-8e87-304a1d0de66e.png)
  1. Room Database를 이용하여 즐겨
