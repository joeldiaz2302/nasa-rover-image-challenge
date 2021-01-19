import React from 'react';
import ReactDOM from 'react-dom';
import 'semantic-ui-css/semantic.min.css';
import './app.css';
import reportWebVitals from './reportWebVitals';

import AppRouter from './router.jsx';
import { createStore, applyMiddleware } from 'redux';
import reducers from './reducers';
import thunk from 'redux-thunk';

const store = createStore(reducers, applyMiddleware(thunk));

ReactDOM.render(<AppRouter store={store} />, document.getElementById('root'));

reportWebVitals();
