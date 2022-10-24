const roles = {
    SUPERADMIN: { rank: 30, name: 'SUPERADMIN', displayName: 'Super Administrateur' },
    SUPERVISOR: { rank: 20, name: 'SUPERVISOR', displayName: 'Superviseur'          },
    USER:       { rank: 10, name: 'USER'      , displayName: 'Utilisateur'          },
    NONE:       { rank:  0, name: 'NONE'      , displayName: 'N/A'                  },
};


/**
 * The parse function is defined this way because it needs to have enumarable to false.
 * If not, the function itself will be iterated on, and a user with the role name 'parse' or 'value'
 * will be given this function as a role (instead of NONE).
 */
Object.defineProperty(roles, 'parse', {
    enumerable: false,
    value(roleName) {
        for(const role of Object.values(this)) {
            if(role.name === roleName) {
                return role;
            }
        }

        return this.NONE;
    },
});

export default roles;