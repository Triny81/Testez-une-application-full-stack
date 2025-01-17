describe('NotFoundComponent', () => {
    beforeEach(() => {
        cy.visit('/non-existing-route'); 
    });

    it('should display the not found message', () => {
        cy.contains('Page not found !').should('be.visible');
    });
});

it('should render NotFoundComponent for invalid routes', () => {
    cy.visit('/non-existing-route');

    cy.url().should('include', '/404');

    cy.contains('Page not found !').should('be.visible');
});
