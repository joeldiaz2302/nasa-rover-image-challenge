import React, {Component} from 'react';
import {Router, Route, Switch} from 'react-router';
import AppPage from './AppPage.jsx';
import { createBrowserHistory } from 'history';
import { Provider } from 'react-redux';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class AppRouter extends Component
{
  render(){
    return (
      <Provider {...this.props}>
        <ToastContainer />
        <Router history={createBrowserHistory()}>
          <div>
            <Switch>
              <Route exact path="/" component={AppPage}/>
            </Switch>
          </div>
        </Router>
      </Provider>
    );
  }
}

export default AppRouter;