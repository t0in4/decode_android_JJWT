# decode_android_JJWT

when i making POST request to get a iam_token - the IntelliJ Idea's token return a iam_token, 
but the Android Studio's token return an error "JWT signature validation fails".

The orders of payload is not equal - data is equal but order of representation is not.

***

"Here is what I would recommend.
Write some example code in your IntelliJ app, to decode the token:
Something like:

Jwts.parserBuilder()
  ... // your settings
  .build()
  .parse(jwtTokenString);
Once you have that working, use the token string created by your Android app.
If this fails, it may help narrow down the actual cause."
