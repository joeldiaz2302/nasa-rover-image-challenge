import React from 'react';
import { withRouter } from 'react-router';
import { RoverImagesActions } from '../actions';
import { connect } from 'react-redux';
import { Container, Segment, Image, Label, Pagination } from 'semantic-ui-react';
import {LoadingOverlay} from "./";


class RoversImagesList extends React.Component
{
	constructor(props){
		super(props);

    	if(this.props.rover){
			RoverImagesActions.setUrl(`rover/${this.props.rover}/images`);
    	}else{
			RoverImagesActions.setUrl(`rover/images`);
    	}
    	this.handlePaginationChange = this.handlePaginationChange.bind(this);
		this.state = {
			images: null,
			fetched_all: false,
			image: 0
		};
		this.props.getImages();
	}

	componentDidUpdate(prevProps, prevState) {
    	if(this.props.rover !== prevProps.rover){
    		if(this.props.rover){
				RoverImagesActions.setUrl(`rover/${this.props.rover}/images`);
    		}else{
				RoverImagesActions.setUrl(`rover/images`);
    		}
			this.props.getImages();
    	}
    	if(this.props.camera !== prevProps.camera){
    		if(this.props.camera){
				RoverImagesActions.setUrl(`rover/${this.props.rover}/images/${this.props.camera}`);
    		}else{
	    		if(this.props.rover){
					RoverImagesActions.setUrl(`rover/${this.props.rover}/images`);
	    		}else{
					RoverImagesActions.setUrl(`rover/images`);
	    		}
    		}
			this.props.getImages();
    	}
		if(!this.props.images.fetching_all && prevProps.images.fetching_all){
			this.setState({images: this.props.images && this.props.images.all_data ? this.props.images.all_data : [], image: 0});
		}
	}

	handlePaginationChange(el, { activePage }){
		this.setState({image: activePage});
	}

	render(){
		return (
			<Container>
				{ this.state.images === null ?
					<Segment>
						<LoadingOverlay message="Loading Images..." />
					</Segment>
				:
					<Segment>
						{this.state.images.length > 0 ?
							<Container>
								<Container style={{height: "500px"}}>
									<Image src={`${process.env.REACT_APP_API_LOCATION}/${this.state.images[this.state.image]}`}
											style={{maxHeight: "350px", margin: "auto"}} />
								</Container>
								<Container>
									<div className="paginationContainer">
								        <Pagination
											activePage={this.state.image}
											boundaryRange={1}
											onPageChange={this.handlePaginationChange}
											siblingRange={1}
											totalPages={this.state.images.length - 1}/>
									</div>
								</Container>
							</Container>
							:
							<Label>No Images Found</Label>
						}
					</Segment>
				}
			</Container>
		);
	}
}

const mapStateToProps = (state, ownProps) => {
	return {
		images: state.images
	};
}

const mapDispatchToProps = (dispatch, ownProps) => {
	return {
		getImages: () => {
			dispatch(RoverImagesActions.index());
		}
	};
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(RoversImagesList));

