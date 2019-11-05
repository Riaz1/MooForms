//
//  ViewController.swift
//  MooFormsSingleView
//
//  Created by mac on 11/5/19.
//  Copyright © 2019 mac. All rights reserved.
//

import UIKit
import WebKit

extension URL {
    
    func appending(_ queryItem: String, value: String?) -> URL {
        
        guard var urlComponents = URLComponents(string: absoluteString) else { return absoluteURL }
        
        // Create array of existing query items
        var queryItems: [URLQueryItem] = urlComponents.queryItems ??  []
        
        // Create query item
        let queryItem = URLQueryItem(name: queryItem, value: value)
        
        // Append the new query item in the existing query items array
        queryItems.append(queryItem)
        
        // Append updated query items array in the url component object
        urlComponents.queryItems = queryItems
        
        // Returns the url from new url components
        return urlComponents.url!
    }
}

class ViewController: UIViewController, WKNavigationDelegate {
    var webView: WKWebView!

    //TODO: get the below information from your MooForms account (integration page)
    var domain = ""
    var publicURL = ""
    var privateURL = ""
    var privateKey = ""
    var usePrivateForm = false //change to true if using a private form
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        var appID = UserDefaults.standard.string(forKey: "appID")
        if (appID == nil) {
            appID = UUID().uuidString
            UserDefaults.standard.set(appID, forKey: "appID")
        }
        
        //are we using the private or public url?
        var formURL:URL
        if (usePrivateForm) {
            formURL = URL(string: privateURL)!
            formURL = formURL.appending("app_id", value: appID)
            formURL = formURL.appending("key", value: privateKey)
        } else {
            formURL = URL(string: publicURL)!
            formURL = formURL.appending("app_id", value: appID)
        }
        
        webView.configuration.preferences.javaScriptEnabled = true
        webView.load(URLRequest(url: formURL))
    }

    override func loadView() {
        webView = WKWebView()
        webView.navigationDelegate = self
        view = webView
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        title = webView.title
    }
    

}

