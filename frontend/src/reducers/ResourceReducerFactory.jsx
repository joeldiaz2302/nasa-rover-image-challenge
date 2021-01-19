export default function ResourceReducerFactory(actionModel) {
	let reducer = null;

	if(actionModel) {
		reducer = function (state = {}, action, status = null){
			let newState = Object.assign({}, {}, state);

			switch (action.type){
				case actionModel.FETCH_ALL:
					newState.fetching_all = true;
					newState.all_data = [];
					newState.errors = null;
					newState.has_errors = false;
					break;
				case actionModel.FETCH_ALL_SUCCESS:
					newState.fetching_all = false;
					newState.all_data = action.data;
					newState.errors = null;
					newState.has_errors = false;
					break;
				case actionModel.FETCH_ALL_FAIL:
					newState.fetching_all = false;
					newState.all_data = [];
					newState.errors = null;
					newState.has_errors = false;
					if(action.errors){
						newState.has_errors = true;
						newState.errors = action.errors;
					}
					break;
				case actionModel.FETCH_ONE:
					newState.fetching_one = true;
					newState.data = null;
					newState.errors = null;
					newState.has_errors = false;
					break;
				case actionModel.FETCH_ONE_SUCCESS:
					newState.fetching_one = false;
					newState.data = action.data;
					newState.errors = null;
					newState.has_errors = false;
					break;
				case actionModel.FETCH_ONE_FAIL:
					newState.fetching_one = false;
					newState.data = null;
					newState.errors = null;
					newState.has_errors = false;
					if(action.errors){
						newState.has_errors = true;
						newState.errors = action.errors;
					}
					break;

				case actionModel.FETCH_METADATA:
					newState.fetching_metadata = true;
					newState.metadata = null;
					newState.errors = null;
					newState.has_errors = false;
					break;
				case actionModel.FETCH_METADATA_SUCCESS:
					newState.fetching_metadata = false;
					newState.metadata = action.data;
					newState.errors = null;
					newState.has_errors = false;
					break;
				case actionModel.FETCH_METADATA_FAIL:
					newState.fetching_metadata = false;
					newState.metadata = null;
					newState.errors = null;
					newState.has_errors = false;
					if(action.errors){
						newState.has_errors = true;
						newState.errors = action.errors;
					}
					break;
				case actionModel.STORE:
				case actionModel.UPDATE:
					newState.submitting = true;
					newState.submitted = false;
					newState.written = null;
					newState.errors = [];
					newState.has_errors = false;
					break;
				case actionModel.STORE_SUCCESS:
				case actionModel.UPDATE_SUCCESS:
					newState.submitting = false;
					newState.submitted = true;
					newState.written = action.data;
					newState.errors = [];
					newState.has_errors = false;
					break;
				case actionModel.STORE_FAIL:
				case actionModel.UPDATE_FAIL:
					newState.submitting = false;
					newState.submitted = false;
					newState.written = null;
					newState.errors = [];
					newState.has_errors = false;
					if(action.errors){
						newState.has_errors = true;
						newState.errors = action.errors;
					}
					break;
				case actionModel.DELETE:
					break;
				case actionModel.DELETE_SUCCESS:
					break;
				case actionModel.DELETE_FAIL:
					break;
				default:
					break;
			}
			return newState;
		}
	}
	return reducer;
}

