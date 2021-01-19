import React from 'react';
import { Dimmer, Loader, Segment } from 'semantic-ui-react';

class LoadingOverlay extends React.Component
{
	render(){
		let msg = this.props.message ? this.props.message : "Loading...";
		return (
			<div className={this.props.containerClass}>
				<Segment style={{width: "100%", height: "45px", margin: "0", border: "none", boxShadow: "none"}}>
			    	<Dimmer active inverted>
			        	<Loader inverted  size='mini'>{msg}</Loader>
			    	</Dimmer>
		    	</Segment>
			</div>
		);
	}
}

export default LoadingOverlay;

