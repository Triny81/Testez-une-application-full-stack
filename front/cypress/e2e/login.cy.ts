describe('Login spec', () => {
  it('should login successfully', () => {
    cy.visit('/login');
    
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');
  });

  it('should loggout after being logged', () => {
    cy.contains('Logout').should('be.visible');

    cy.contains('Logout').click();

    // Vérifier que l'état de connexion n'est plus actif
    cy.contains('Login').should('be.visible');
    cy.contains('Register').should('be.visible');
    cy.contains('Logout').should('not.exist');
  });
  
  it('should disable the submit button when the form is invalid', () => {
    cy.visit('/login');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should disable the submit button when the form has an invalid email', () => {
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('invalid-email'); // Email invalide format
    cy.get('button[type="submit"]').should('be.disabled'); // Bouton est toujours désactivé ?
  });

  it('should disable the submit button when the form has no password', () => {
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('yoga@studio.com'); 
    cy.get('button[type="submit"]').should('be.disabled'); // Bouton est toujours désactivé ?
  });

  it('should display an error message if login fails', () => {
    cy.visit('/login');
  
    cy.get('input[formControlName="email"]').type('test@example.com'); // unknwown email
    cy.get('input[formControlName="password"]').type('password123'); // known password

    // Intercepter la requête pour simuler une erreur
    cy.intercept('POST', '/api/auth/login', (req) => {
      req.reply({
        statusCode: 401,
        body: { message: 'Unauthorized' },
      });
    }).as('loginRequest');
  
    cy.get('button[type="submit"]').click(); // Sumbit the form
  
    cy.wait('@loginRequest');
    cy.contains('An error occurred').should('be.visible');
  });
});
