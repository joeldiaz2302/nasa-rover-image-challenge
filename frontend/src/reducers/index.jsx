import { combineReducers } from 'redux';
import ResourceReducerFactory from './ResourceReducerFactory';
import {resources} from '../actions';

const reducerMap = {};

//generate a reducer for each genericly created action)
Object.keys(resources).forEach(key => {
	let reducer = ResourceReducerFactory(resources[key]);
	reducerMap[key] = reducer;
});

//combine all reducers
const reducers = combineReducers(reducerMap);

export default reducers ;
