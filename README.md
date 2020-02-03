# MooForms
Sample apps on how to integrate MooForms on various mobile platforms.

## Running the sample code
1. Download and import the project into your IDE
2. Update the code to use the values from your MooForms account:

   * domain (for Android apps)
   * formURL
   
   * formKey OR renderKey if you're using a render
   * set useSaveableForm to true (if using a saveable form)
   
3. Run the sample

## How it works

MooForms is integrated into a mobile app using a WebView component. The form is rendered within the WebView using the formURL + various GET parameters.
