describe('Register spec', () => {
    it('should display the register form and disable submit by default', () => {
        cy.visit('/register');

        // Vérifiez que tous les champs sont présents
        cy.get('input[formControlName="firstName"]').should('exist');
        cy.get('input[formControlName="lastName"]').should('exist');
        cy.get('input[formControlName="email"]').should('exist');
        cy.get('input[formControlName="password"]').should('exist');

        // Vérifiez que le bouton "Submit" est désactivé par défaut
        cy.get('button[type="submit"]').should('be.disabled');
    });

    it('should disable the submit button when the form has an invalid email', () => {
        cy.visit('/register');
        cy.get('input[formControlName="email"]').type('invalid-email'); // Email invalide format
        cy.get('button[type="submit"]').should('be.disabled'); // Bouton est toujours désactivé ?
    });

    it('should disable submit if required fields are missing', () => {
        cy.visit('/register');

        cy.get('input[formControlName="firstName"]').type('John');
        cy.get('input[formControlName="email"]').type('john.doe@example.com');
        
        cy.get('button[type="submit"]').should('be.disabled');
    });

    it('should register successfully with valid inputs', () => {
        cy.visit('/register');

        cy.get('input[formControlName="firstName"]').type('John');
        cy.get('input[formControlName="lastName"]').type('Doe');
        cy.get('input[formControlName="email"]').type('john.doe@example.com');
        cy.get('input[formControlName="password"]').type('password123');

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 201,
            body: { message: 'User registered successfully' },
        }).as('registerRequest');

        cy.get('button[type="submit"]').click();

        cy.wait('@registerRequest').its('response.statusCode').should('eq', 201);

        cy.url().should('include', '/login'); // redirection vers la page de login
    });

    it('should display an error message if registration fails', () => {
        cy.visit('/register');

        // Remplir les champs avec des données valides
        cy.get('input[formControlName="firstName"]').type('John');
        cy.get('input[formControlName="lastName"]').type('Doe');
        cy.get('input[formControlName="email"]').type('john.doe@example.com');
        cy.get('input[formControlName="password"]').type('password123');

        // Intercepter la requête pour simuler une erreur
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: { message: 'Registration failed' },
        }).as('registerRequest');

        // Soumettre le formulaire
        cy.get('button[type="submit"]').click();

        // Attendre la requête et vérifier qu'une erreur est affichée
        cy.wait('@registerRequest');
        cy.contains('An error occurred').should('be.visible');
    });
});