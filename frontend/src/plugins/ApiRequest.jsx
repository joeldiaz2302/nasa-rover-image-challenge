import axios from "axios";

function addHeaders(params, headers){
	params.headers = Object.assign({
		// "Access-Control-Allow-Origin": "*",
	}, headers, {});

	return params;
}

function getUrl(location){
	return `${process.env.REACT_APP_API_HOST}/api/${location}`;
}

const ApiRequest = {
	get: function(url, data, success, fail) {
		let params = {params: data};
		return axios.get(`${getUrl(url)}`, addHeaders(params)).then(success).catch(fail);
	},
	post: function(url, data, success, fail) {
		let contentLength = JSON.stringify(data).length + 2000;
		let headers = {};

		if(data.getHeaders){
			headers = data.getHeaders();
		}
		if(data.file){
			headers['Content-Length'] = contentLength;
			headers['Content-Type'] = 'multipart/form-data';
		}
		return axios.post(`${getUrl(url)}`, data, addHeaders(headers)).then(success).catch(fail);
	},
	put: function(url, params, success, fail) {
		return axios.put(`${getUrl(url)}`, params, addHeaders({})).then(success).catch(fail);
	},
	patch: function(url, params, success, fail) {
		return axios.put(`${getUrl(url)}`, params, addHeaders({})).then(success).catch(fail);
	},
	delete: function(url, data, success, fail) {
		let params = {params: data};
		return axios.delete(`${getUrl(url)}`, addHeaders(params)).then(success).catch(fail);
	}
};

export default ApiRequest;
