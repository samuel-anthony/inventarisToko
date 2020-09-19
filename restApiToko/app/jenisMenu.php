<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class jenisMenu extends Model
{
    protected $primaryKey = 'jenis_menu_id';

    public function jenisMenuDetails(){
        return $this->hasMany('App\jenisMenuDetail','jenis_menu_id','jenis_menu_id');
    }
}
