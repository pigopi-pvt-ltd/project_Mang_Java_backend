package com.pigopi.vault.exception;


	public class ResourceNotFoundException extends RuntimeException {

		
		private static final long serialVersionUID = 1L;// optional just for java satisfaction ?

		public ResourceNotFoundException(String message) {
			super(message);
		}

	
}
