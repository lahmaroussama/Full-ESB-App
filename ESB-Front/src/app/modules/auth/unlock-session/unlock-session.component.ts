import { NgIf } from '@angular/common';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormsModule, NgForm, ReactiveFormsModule, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { FuseAlertComponent, FuseAlertType } from '@fuse/components/alert';
import { AuthService } from 'app/core/auth/auth.service';
import { UserService } from 'app/core/user/user.service';

@Component({
    selector     : 'auth-unlock-session',
    templateUrl  : './unlock-session.component.html',
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations,
    standalone   : true,
    imports      : [NgIf, FuseAlertComponent, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, RouterLink],
})
export class AuthUnlockSessionComponent implements OnInit
{
    @ViewChild('unlockSessionNgForm') unlockSessionNgForm: NgForm;

    alert: { type: FuseAlertType; message: string } = {
        type   : 'success',
        message: '',
    };
    username: string;
    name: string;
    showAlert: boolean = false;
    unlockSessionForm: UntypedFormGroup;

    /**
     * Constructor
     */
    constructor(
        private _activatedRoute: ActivatedRoute,
        private _authService: AuthService,
        private _formBuilder: UntypedFormBuilder,
        private _router: Router,
        private _userService: UserService,
    )
    {
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
     * On init
     */
    ngOnInit(): void
    {
        // Get the user's username
        this._userService.user$.subscribe((user) =>
        {
            this.username = user.name; // Use name as username since that's how we store it
            this.name = user.name; // Set name property for template
        });

        // Create the form
        this.unlockSessionForm = this._formBuilder.group({
            username: [this.username || '', [Validators.required]],
            password: ['', [Validators.required]],
        });
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Unlock
     */
    unlock(): void
    {
        // Return if the form is invalid
        if ( this.unlockSessionForm.invalid )
        {
            return;
        }

        // Disable the form
        this.unlockSessionForm.disable();

        // Hide the alert
        this.showAlert = false;

        // Prepare the unlock data matching backend LoginRequest format
        const unlockData = {
            username: this.unlockSessionForm.get('username').value,
            password: this.unlockSessionForm.get('password').value
        };

        this._authService.unlockSession(unlockData).subscribe(
            (response) =>
            {
                // Show success message
                this.alert = {
                    type: 'success',
                    message: 'Session unlocked successfully!'
                };
                this.showAlert = true;

                // Set the redirect url.
                // The '/signed-in-redirect' is a dummy url to catch the request and redirect the user
                // to the correct page after a successful sign in. This way, that url can be set via
                // routing file and we don't have to touch here.
                const redirectURL = this._activatedRoute.snapshot.queryParamMap.get('redirectURL') || '/signed-in-redirect';

                // Navigate to the redirect url after a short delay
                setTimeout(() => {
                    this._router.navigateByUrl(redirectURL);
                }, 1000);

            },
            (error) =>
            {
                // Re-enable the form
                this.unlockSessionForm.enable();

                // Reset the form
                this.unlockSessionNgForm.resetForm({
                    username: {
                        value: this.username,
                        disabled: false,
                    },
                });

                // Set the alert with backend error message
                let errorMessage = 'Invalid username or password';
                
                if (error.error && error.error.message) {
                    errorMessage = error.error.message;
                } else if (error.status === 401) {
                    errorMessage = 'Invalid credentials. Please check your username and password.';
                } else if (error.status === 400) {
                    errorMessage = 'Invalid data provided. Please check your input.';
                }

                this.alert = {
                    type: 'error',
                    message: errorMessage
                };

                // Show the alert
                this.showAlert = true;
            },
        );
    }
}
