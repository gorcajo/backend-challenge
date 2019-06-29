class Application extends React.Component {
	
	constructor() {
		super();
		
		this.state = {
			basketId: null,
			totalAmount: 0.0,
		};

		this.createNewBasket = this.createNewBasket.bind(this);
		this.addProductToBasket = this.addProductToBasket.bind(this);
		this.refreshTotalAmount = this.refreshTotalAmount.bind(this);
		this.deleteBasket = this.deleteBasket.bind(this);
	}
	
	componentDidMount() {
		this.createNewBasket();
	}
	
	createNewBasket() {
		fetch("/api/v1/basket", {
			method: "POST",
			headers: {
				"Content-Type": "application/json;charset=UTF-8"
			}
		})
		.then(response => response.text())
		.then((data) => {
			this.setState({basketId: data})
			this.refreshTotalAmount();
		});
	}

	addProductToBasket(productCode) {
		fetch("/api/v1/basket/" + this.state.basketId, {
			method: "PUT",
			headers: {
				"Content-Type": "application/json;charset=UTF-8"
			},
			body: productCode
		})
		.then(() => {
			this.refreshTotalAmount();
		});
	}

	refreshTotalAmount() {
		fetch("/api/v1/basket/" + this.state.basketId + "/totalamount", {
			method: "GET",
			headers: {
				"Content-Type": "application/json;charset=UTF-8"
			}
		})
		.then(response => response.text())
		.then((data) => {
			this.setState({
				totalAmount: data
			});
		});
	}
	
	deleteBasket() {
		fetch("/api/v1/basket/" + this.state.basketId, {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json;charset=UTF-8"
			}
		})
		.then(() => {
			this.createNewBasket();
		});
	}
	
	renderBasket(totalAmount) {
		return <Basket onBasketDelete={this.deleteBasket} totalAmount={this.state.totalAmount}/>;
	}
	
	render() {
		return (
		    <div>
			    <div className='row'>
				    <div className='col-md-12'>
				        <div className='header'>
				            <h1>Backend Challenge</h1>
				            <h4><i>Guillermo Orcajo García</i></h4>
				        </div>
				    </div>
				</div>
				
			    <div className='row'>
			        <ProductsArea onProductAdd={this.addProductToBasket} />
			        {this.renderBasket()}
			    </div>
		    </div>
		);
	}
}

class ProductsArea extends React.Component {

    render() {
        return (
            <div className='col-md-8'>
                <div className='card'>
                    <div className='card-header bg-primary text-white section-header'>Products</div>
                    
                    <div className='card-body'>
                        <div className='row'>
                            <div className='col-md-4'>
                                <Product
                                	code={"VOUCHER"}
                                	name={"Cabify Voucher"}
                                	price={5.0}
                                	onProductAdd={this.props.onProductAdd}
                                />
                            </div>
                                
                            <div className='col-md-4'>
                            	<Product
	                            	code={"TSHIRT"}
	                            	name={"Cabify T-Shirt"}
	                            	price={20.0}
	                            	onProductAdd={this.props.onProductAdd}
                            	/>
                            </div>
                            	
                            <div className='col-md-4'>
                        		<Product
	                        		code={"MUG"}
	                        		name={"Cabify Coffee Mug"}
	                        		price={7.5}
	                        		onProductAdd={this.props.onProductAdd}
                        		/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class Product extends React.Component {
	
    render() {
    	let code = this.props.code;
    	let name = this.props.name;
    	let price = parseFloat(this.props.price).toFixed(2);
    	
        return (
            <div className='card'>
                <div className='card-header product-header'>{name}</div>
                <div className='card-body'>
                    <p className='product-amount'>{price} €</p>

                    <button
                    	type='button'
                		className='btn btn-success btn-block'
            			onClick={() => this.props.onProductAdd(code) }>
                    		Add to basket
                    </button>
                </div>
            </div>
        );
    }
}

class Basket extends React.Component {

    render() {
    	let totalAmount = parseFloat(this.props.totalAmount).toFixed(2);
    	
        return (
            <div className='col-md-3 offset-md-1'>
                <div className='card'>
                    <div className='card-header bg-primary text-white section-header'>Basket</div>
                    
                    <div className='card-body'>
                        <div className='row'>
                            <div className='col-md-12'>
                                <p id='total-amount' className='basket-amount'>{totalAmount} €</p>
                            </div>
                        </div>
                        
                        <div className='row'>
                            <div className='col-md-12'>
	                            <button
	                            	type='button'
                            		className='btn btn-danger btn-block'
                        			onClick={this.props.onBasketDelete}>
		                        		Empty basket
		                        </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

// ========================================

ReactDOM.render(
	<Application />,
    document.getElementById('react-root')
);
