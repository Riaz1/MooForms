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
    var formURL = ""
    var formKey = ""
    var renderKey = ""
    var useSaveableForm = false //change to true if using a saveable form
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        var appID = UserDefaults.standard.string(forKey: "appID")
        if (appID == nil) {
            appID = UUID().uuidString
            UserDefaults.standard.set(appID, forKey: "appID")
        }
        
        var URLToUse:URL
        URLToUse = URL(string: formURL)!
        
        //are we using a form key or render key?
        if (!formKey.isEmpty) {
            URLToUse = URLToUse.appending("form_key", value: formKey)
        } else if (!renderKey.isEmpty) {
            URLToUse = URLToUse.appending("render_key", value: renderKey)
        }
        
        if (useSaveableForm) {
            URLToUse = URLToUse.appending("app_id", value: appID)
        }
        
        webView.configuration.preferences.javaScriptEnabled = true
        webView.load(URLRequest(url: URLToUse))
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

