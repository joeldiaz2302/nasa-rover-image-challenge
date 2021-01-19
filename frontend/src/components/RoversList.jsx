import React from 'react';
import { withRouter } from 'react-router';
import { RoverActions, ImageUploadActions } from '../actions';
import { connect } from 'react-redux';
import { InputFile } from 'semantic-ui-react-input-file';
import { Header, Segment, Label, Form, Menu, Dropdown, Button, Popup } from 'semantic-ui-react';
import {LoadingOverlay} from "./";
import { toast } from 'react-toastify';
import $ from 'jquery'; 

class RoversList extends React.Component
{
	constructor(props){
		super(props);
    	this.onSelectRover = this.onSelectRover.bind(this);
    	this.onSelectCamera = this.onSelectCamera.bind(this);
    	this.onFileAdded = this.onFileAdded.bind(this);
    	this.uploadFile = this.uploadFile.bind(this);
		this.state = {
			rovers: null,
			fetched_all: false,
			page: 0,
			rover: null,
			camera: null,
			file: null
		};
		props.getRovers();
	}

	onSelectRover(el, {value}){
		var rov = this.state.rovers.find(rover => rover.name === value) || null;

		this.setState({rover: rov});
		if(this.props.onRoverSelected){
			this.props.onRoverSelected(rov ? rov.name : null);
		}
	}

	onSelectCamera(el, {value}){
		var camera = this.state.rover ? this.state.rover.cameras.find(cam => cam.name === value) : null;
		this.setState({camera: camera});
		if(this.props.onCameraSelected){
			this.props.onCameraSelected(camera ? camera.name : null);
		}
	}

	onFileAdded(el, item){
		this.setState({file: el.target.files[0]});

	}

	uploadFile(el, item){
		let file = this.state.file;
		if(this.state.rover !== null){
			if(this.state.camera !== null){
				//upload a dates file and pull all images for a rovers specific camera for those dates
				ImageUploadActions.setUrl(`rover/${this.state.rover.name}/dates_upload/${this.state.camera.name}`);
			}else{
				//upload a dates file and pull all images for a rover for those dates
				ImageUploadActions.setUrl(`rover/${this.state.rover.name}/dates_upload`);
			}
		}else{
			//upload a dates file and pull all images for all rovers for those dates
			ImageUploadActions.setUrl(`rover/dates_upload`);
		}

  		let formData = new FormData();
  		formData.append("file", file);
		this.props.uploadFile(formData);
		this.setState({file: null});
		//React weirdness, need to forde file input empty
		$("#input-control-id").val(''); 
	}

	componentDidUpdate(prevProps, prevState){
		if(prevProps.uploader.submitting === true && prevProps.uploader.submitting !== this.props.uploader.submitting){
			toast(this.props.uploader.has_errors ? this.props.uploader.errors : "File Uploaded Successfully");
		}
		if(!this.props.rovers.fetching_all && prevProps.rovers.fetching_all){
			this.setState({rovers: this.props.rovers && this.props.rovers.all_data ? this.props.rovers.all_data.rovers : []});
		}
	}

	render(){
		let cameras = this.state.rover ? this.state.rover.cameras.map((camera) => {return { key: camera.id, text: camera.full_name, value: camera.name }}) : [];
		return (
			<Segment>
				<Header>
					Rover/Camera Select
				</Header>
				{this.props.onRoverSelected !== null &&
					<Label>Select a rover to view its downloaded images</Label>
				}
				{ this.state.rovers === null ?
				<LoadingOverlay message="Loading Rovers..." />
				:
				<Form>
					<Menu>
						<Menu.Item>
							<Dropdown
								placeholder='Select a Rover'
								fluid
								selection
								clearable
								style={{width: "160px"}}
								options={this.state.rovers ? this.state.rovers.map((rover) => {return { key: rover.id, text: rover.name, value: rover.name }}) : []}
								onChange={this.onSelectRover} />
						</Menu.Item>
						<Menu.Item>
							<Dropdown
								placeholder='Select a camera'
								fluid
								selection
								clearable
								style={{width: "380px"}}
								options={cameras}
								onChange={this.onSelectCamera} />
						</Menu.Item>
						<Popup trigger={
							<Menu.Item>
								<InputFile
									button={{}}
									input={{
									    id: 'input-control-id',
									    onChange: this.onFileAdded,
									    accept: "text/plain"
									}} />
								<Button style={{margin: "0 15px"}}
										disabled={this.state.file === null}
										onClick={this.uploadFile}>Upload</Button>
							</Menu.Item>
						}>
							<Popup.Content>
								This will upload a file meant to automatically download images based on the dates in the file.
								If you have the rover or camera set it will limit processing to those specifics. This it to reduce load.
								Unselect all if you want to process all available images for the dates in the file.
							</Popup.Content>
						</Popup>
					</Menu>
				</Form>
				}
			</Segment>
		);
	}
}


const mapStateToProps = (state, ownProps) => {
	return {
		rovers: state.rovers,
		uploader: state.image_uploader
	};
}

const mapDispatchToProps = (dispatch, ownProps) => {
	return {
		getRovers: () => {
			dispatch(RoverActions.index());
		},
		uploadFile: (data) => {
			dispatch(ImageUploadActions.store(data));
		}
	};
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(RoversList));

