import { ApiRequest } from '../plugins';

export default class ResourceActions {

	constructor(token, url = null){
		this.token = token;
		this.URL = `${url !== null ? url : this.token}`;
		this.FETCH_ONE = `FETCH_ONE_${this.token.toUpperCase()}`;
		this.FETCH_ONE_SUCCESS = `FETCH_ONE_${this.token.toUpperCase()}_SUCCESS`;
		this.FETCH_ONE_FAIL = `FETCH_ONE_${this.token.toUpperCase()}_FAIL`;
		this.FETCH_ALL = `FETCH_ALL_${this.token.toUpperCase()}`;
		this.FETCH_ALL_SUCCESS = `FETCH_ALL_${this.token.toUpperCase()}_SUCCESS`;
		this.FETCH_ALL_FAIL = `FETCH_ALL_${this.token.toUpperCase()}_FAIL`;
		this.FETCH_METADATA = `FETCH_METADATA_${this.token.toUpperCase()}`;
		this.FETCH_METADATA_SUCCESS = `FETCH_METADATA_${this.token.toUpperCase()}_SUCCESS`;
		this.FETCH_METADATA_FAIL = `FETCH_METADATA_${this.token.toUpperCase()}_FAIL`;
		this.STORE = `STORE_${this.token.toUpperCase()}`;
		this.STORE_SUCCESS = `STORE_SUCCESS_${this.token.toUpperCase()}`;
		this.STORE_FAIL = `STORE_FAIL_${this.token.toUpperCase()}`;
		this.UPDATE = `UPDATE_${this.token.toUpperCase()}`;
		this.UPDATE_SUCCESS = `UPDATE_${this.token.toUpperCase()}_SUCCESS`;
		this.UPDATE_FAIL = `UPDATE_${this.token.toUpperCase()}_FAIL`;
		this.DELETE = `DELETE_${this.token.toUpperCase()}`;
		this.DELETE_SUCCESS = `DELETE_${this.token.toUpperCase()}_SUCCESS`;
		this.DELETE_FAIL = `DELETE_${this.token.toUpperCase()}_FAIL`;
		
		this.sendRequest = (request, url, data, send_event, success_event, fail_event) => {
			let self = this;
			return dispatch => {
				let success = function(response){
					self.handleSuccess(response, success_event, dispatch);
				}
				let fail = function(err){
					self.handleFail(err, fail_event, dispatch);
				}
				dispatch({type: send_event});
				request(url, data, success, fail);
			};
		}

		this.metadata = () => {
			let url = `${this.URL}/metadata`;
			return this.sendRequest(
				ApiRequest.get,
				url,
				{},
				this.FETCH_METADATA,
				this.FETCH_METADATA_SUCCESS,
				this.FETCH_METADATA_FAIL
			);
		}

		//GET
		this.index = (data = {}, event) => {
			let url = `${this.URL}`;
			return this.sendRequest(
				ApiRequest.get,
				url,
				data,
				this.FETCH_ALL,
				this.FETCH_ALL_SUCCESS,
				this.FETCH_ALL_FAIL
			);
		};
		
		//POST
		this.store = (data) => {
			let url = `${this.URL}`;
			return this.sendRequest(
				ApiRequest.post,
				url,
				data,
				this.STORE,
				this.STORE_SUCCESS,
				this.STORE_FAIL
			);
		};
		
		//GET
		this.show = (id, data = {}) => {
			let url = `${this.URL}/${id}`;
			return this.sendRequest(
				ApiRequest.get,
				url,
				data,
				this.FETCH_ONE,
				this.FETCH_ONE_SUCCESS,
				this.FETCH_ONE_FAIL
			);
		};
				
		//PATCH
		this.update = (id, data) => {
			let url = `${this.URL}/${id}`;
			return this.sendRequest(
				ApiRequest.patch,
				url,
				data,
				this.UPDATE,
				this.UPDATE_SUCCESS,
				this.UPDATE_FAIL
			);
		};
		
		//DELETE
		this.destroy = (id, data) => {
			let url = `${this.URL}/${id}`;
			return this.sendRequest(
				ApiRequest.delete,
				url,
				data,
				this.DELETE,
				this.DELETE_SUCCESS,
				this.DELETE_FAIL
			);
		};

		this.formatFetchedDataset = (data) => {
			return data;
		}

		this.handleFail = (err, type, dispatch) => {
			let errors = [];
			let status = null;

			if(err.response){
				if(err.response.data && err.response.data.errors){
					errors = this.parseErrors(err.response.data.errors);
				}
				if(err.response.message){
					errors.push(err.response.message);
				}
				// eslint-disable-next-line
				if (err.response.status == 403){
					window.location.href = '/login';
				}
				status = err.response.status;
			} else{
				errors.push("Request did not recieve a response");
			}
			dispatch({type: type, errors: errors, status: status});
		}

		this.handleSuccess = (response, type, dispatch) =>{
			dispatch({
				type: type,
				data: this.formatFetchedDataset(response.data),
				status: response.status
			});
		}

		this.parseErrors = function(responseErrors){
			let errors = [];
			if(responseErrors){
				Object.keys(responseErrors).forEach(key => {
					if(Array.isArray(responseErrors[key])){
						responseErrors[key].forEach(err => {
							errors.push(err);
						});
					}else{
						errors.push(responseErrors[key]);
					}
				});
			}
			return errors;
		}

		//please only use this when accessing strange api endpoints like messages located at thread/{thread_id}/messages
		this.setUrl = (url) => {
			this.URL = url;
		}

		this.getUrl = () => {
			return this.URL;
		}

		this.toObject = () => {
			return {
				metadata: this.metadata,
				index: this.index,
				store: this.store,
				show: this.show,
				update: this.update,
				destroy: this.destroy,
				setUrl: this.setUrl,
				getUrl: this.getUrl,
				FETCH_ONE: this.FETCH_ONE,
				FETCH_ONE_SUCCESS: this.FETCH_ONE_SUCCESS,
				FETCH_ONE_FAIL: this.FETCH_ONE_FAIL,
				FETCH_ALL: this.FETCH_ALL,
				FETCH_ALL_SUCCESS: this.FETCH_ALL_SUCCESS,
				FETCH_ALL_FAIL: this.FETCH_ALL_FAIL,
				FETCH_METADATA: this.FETCH_METADATA,
				FETCH_METADATA_SUCCESS: this.FETCH_METADATA_SUCCESS,
				FETCH_METADATA_FAIL: this.FETCH_METADATA_FAIL,
				STORE: this.STORE,
				STORE_SUCCESS: this.STORE_SUCCESS,
				STORE_FAIL: this.STORE_FAIL,
				UPDATE: this.UPDATE,
				UPDATE_SUCCESS: this.UPDATE_SUCCESS,
				UPDATE_FAIL: this.UPDATE_FAIL,
				DELETE: this.DELETE,
				DELETE_SUCCESS: this.DELETE_SUCCESS,
				DELETE_FAIL: this.DELETE_FAIL
			}
		}
	}
}
