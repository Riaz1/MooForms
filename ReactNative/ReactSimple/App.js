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

    let formURL = '';

    //TODO: get the below information from your MooForms account
    let publicURL = '';
    let privateURL = '';
    let privateKey = '';
    let usePrivateForm = false; //change to true if using a private form

    formURL = publicURL;
    if (usePrivateForm) {
      formURL = privateURL;
    }

    formURL = formURL + '?app_id=' + this.state.appID;
    if (usePrivateForm) {
      formURL = formURL + '&key=' + privateKey;
    }

    console.log('form:' + formURL);
    console.log('appID: ' +  this.state.appID);

    let url = {
      uri: formURL
    };
    return (
      <WebView
        source={url}
        style={{ marginTop: 0 }}
      />
    );
  }
}