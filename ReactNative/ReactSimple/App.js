/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Component } from 'react';
import { WebView } from 'react-native-webview';
import DefaultPreference from 'react-native-default-preference';
import UUIDGenerator from 'react-native-uuid-generator';

export default class App extends Component {

  constructor() {
    super();
    this.state = {  
      hasAppID: false 
    };
  }

  async componentDidMount(){
    const uuid = await UUIDGenerator.getRandomUUID();
    let aid = await DefaultPreference.get('appID');

    if (aid == null || aid == '') {
      aid = uuid;
      DefaultPreference.set('appID', aid);
    }

    if (aid != null && aid != '') {
      this.setState({ 
        hasAppID: true, 
        appID: aid 
      });
    }
  }

  render() {

    if (!this.state.hasAppID) {
      return null;
    }
	 
    //TODO: get the below information from your MooForms account
    let URLToUse = '';
    let formURL = '';
    let formKey = '';
    let renderKey = '';
    let useSaveableForm = false; //change to true if using a saveable form, default is submit only
    let URLHasParam = false;

    URLToUse = formURL;
    if (formKey !== '') {
      URLToUse = URLToUse + '?form_key=' + formKey;
      URLHasParam = true;
    } else if (renderKey !== '') {
      URLToUse = URLToUse + '?render_key=' + renderKey;
      URLHasParam = true;
    }

    if (useSaveableForm) {
      if (URLHasParam) {
        URLToUse = URLToUse + '&';
      } else {
        URLToUse = URLToUse + '?';
      }
      URLToUse = URLToUse + 'app_id=' + this.state.appID;
    }

    let url = {
      uri: URLToUse
    };
    return (
      <WebView
        source={url}
        style={{ marginTop: 0 }}
      />
    );
  }
}
